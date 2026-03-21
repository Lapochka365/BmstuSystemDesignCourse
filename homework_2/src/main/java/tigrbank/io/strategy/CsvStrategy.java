package tigrbank.io.strategy;

import org.springframework.stereotype.Component;
import tigrbank.domain.*;
import tigrbank.io.DataExportStrategy;
import tigrbank.io.DataImportStrategy;
import tigrbank.io.FinanceData;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CsvStrategy implements DataExportStrategy, DataImportStrategy {

    private static final String SECTION_ACCOUNTS = "#ACCOUNTS";
    private static final String SECTION_CATEGORIES = "#CATEGORIES";
    private static final String SECTION_OPERATIONS = "#OPERATIONS";

    @Override
    public String getFormat() {
        return "csv";
    }

    @Override
    public void exportData(List<BankAccount> accounts, List<Category> categories,
            List<Operation> operations, String filePath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8))) {

            pw.println(SECTION_ACCOUNTS);
            pw.println("id,name,balance,active");
            for (BankAccount a : accounts) {
                pw.printf("%s,%s,%s,%s%n", a.getId(), escapeCsv(a.getName()), a.getBalance().toPlainString(),
                        a.isActive());
            }

            pw.println(SECTION_CATEGORIES);
            pw.println("id,type,name,active");
            for (Category c : categories) {
                pw.printf("%s,%s,%s,%s%n", c.getId(), c.getType(), escapeCsv(c.getName()), c.isActive());
            }

            pw.println(SECTION_OPERATIONS);
            pw.println("id,type,bankAccountId,amount,date,description,categoryId,active");
            for (Operation op : operations) {
                pw.printf("%s,%s,%s,%s,%s,%s,%s,%s%n",
                        op.getId(), op.getType(), op.getBankAccountId(),
                        op.getAmount().toPlainString(), op.getDate(),
                        escapeCsv(op.getDescription() != null ? op.getDescription() : ""),
                        op.getCategoryId(), op.isActive());
            }
        }
    }

    @Override
    public FinanceData importData(String filePath) throws IOException {
        List<BankAccount> accounts = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Operation> operations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            String currentSection = null;
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;

                if (line.startsWith("#")) {
                    currentSection = line;
                    br.readLine(); // skip header
                    continue;
                }

                String[] parts = splitCsv(line);

                switch (currentSection) {
                    case SECTION_ACCOUNTS -> {
                        BankAccount acc = new BankAccount(
                                UUID.fromString(parts[0]), parts[1], new BigDecimal(parts[2]));
                        if (parts.length > 3)
                            acc.setActive(Boolean.parseBoolean(parts[3]));
                        accounts.add(acc);
                    }

                    case SECTION_CATEGORIES -> {
                        Category cat = new Category(
                                UUID.fromString(parts[0]), OperationType.valueOf(parts[1]), parts[2]);
                        if (parts.length > 3)
                            cat.setActive(Boolean.parseBoolean(parts[3]));
                        categories.add(cat);
                    }
                    case SECTION_OPERATIONS -> {
                        Operation op = new Operation(
                                UUID.fromString(parts[0]),
                                OperationType.valueOf(parts[1]),
                                UUID.fromString(parts[2]),
                                new BigDecimal(parts[3]),
                                LocalDate.parse(parts[4]),
                                parts[5].isEmpty() ? null : parts[5],
                                UUID.fromString(parts[6]));
                        if (parts.length > 7) {
                            op.setActive(Boolean.parseBoolean(parts[7]));
                        }
                        operations.add(op);
                    }
                    default -> {
                        /* ignore */ }
                }
            }
        }
        return new FinanceData(accounts, categories, operations);
    }

    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String[] splitCsv(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (inQuotes) {
                if (ch == '"' && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else if (ch == '"') {
                    inQuotes = false;
                } else {
                    current.append(ch);
                }
            } else {
                if (ch == '"') {
                    inQuotes = true;
                } else if (ch == ',') {
                    result.add(current.toString());
                    current.setLength(0);
                } else {
                    current.append(ch);
                }
            }
        }
        result.add(current.toString());
        return result.toArray(new String[0]);
    }
}

package tigrbank.io.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;
import tigrbank.domain.*;
import tigrbank.io.DataExportStrategy;
import tigrbank.io.DataImportStrategy;
import tigrbank.io.FinanceData;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractJacksonStrategy implements DataExportStrategy, DataImportStrategy {

    protected final ObjectMapper mapper;

    protected AbstractJacksonStrategy(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void exportData(List<BankAccount> accounts, List<Category> categories,
            List<Operation> operations, String filePath) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        root.set("accounts", serializeAccounts(accounts));
        root.set("categories", serializeCategories(categories));
        root.set("operations", serializeOperations(operations));
        mapper.writeValue(new File(filePath), root);
    }

    @Override
    public FinanceData importData(String filePath) throws IOException {
        JsonNode root = mapper.readTree(new File(filePath));
        List<BankAccount> accounts = parseAccounts(root.get("accounts"));
        List<Category> categories = parseCategories(root.get("categories"));
        List<Operation> operations = parseOperations(root.get("operations"));

        return new FinanceData(accounts, categories, operations);
    }

    private ArrayNode serializeAccounts(List<BankAccount> accounts) {
        ArrayNode arr = mapper.createArrayNode();
        for (BankAccount a : accounts) {
            ObjectNode n = mapper.createObjectNode();
            n.put("id", a.getId().toString());
            n.put("name", a.getName());
            n.put("balance", a.getBalance().toPlainString());
            n.put("active", a.isActive());
            arr.add(n);
        }

        return arr;
    }

    private ArrayNode serializeCategories(List<Category> categories) {
        ArrayNode arr = mapper.createArrayNode();
        for (Category c : categories) {
            ObjectNode n = mapper.createObjectNode();
            n.put("id", c.getId().toString());
            n.put("type", c.getType().name());
            n.put("name", c.getName());
            n.put("active", c.isActive());
            arr.add(n);
        }

        return arr;
    }

    private ArrayNode serializeOperations(List<Operation> operations) {
        ArrayNode arr = mapper.createArrayNode();
        for (Operation op : operations) {
            ObjectNode n = mapper.createObjectNode();
            n.put("id", op.getId().toString());
            n.put("type", op.getType().name());
            n.put("bankAccountId", op.getBankAccountId().toString());
            n.put("amount", op.getAmount().toPlainString());
            n.put("date", op.getDate().toString());
            n.put("description", op.getDescription() != null ? op.getDescription() : "");
            n.put("categoryId", op.getCategoryId().toString());
            n.put("active", op.isActive());
            arr.add(n);
        }

        return arr;
    }

    private List<BankAccount> parseAccounts(JsonNode node) {
        List<BankAccount> list = new ArrayList<>();
        if (node == null)
            return list;

        for (JsonNode n : node) {
            BankAccount acc = new BankAccount(
                    UUID.fromString(n.get("id").asText()),
                    n.get("name").asText(),
                    new BigDecimal(n.get("balance").asText()));
            if (n.has("active"))
                acc.setActive(n.get("active").asBoolean());
            list.add(acc);
        }

        return list;
    }

    private List<Category> parseCategories(JsonNode node) {
        List<Category> list = new ArrayList<>();
        if (node == null)
            return list;

        for (JsonNode n : node) {
            Category cat = new Category(
                    UUID.fromString(n.get("id").asText()),
                    OperationType.valueOf(n.get("type").asText()),
                    n.get("name").asText());
            if (n.has("active"))
                cat.setActive(n.get("active").asBoolean());
            list.add(cat);
        }

        return list;
    }

    private List<Operation> parseOperations(JsonNode node) {
        List<Operation> list = new ArrayList<>();
        if (node == null)
            return list;

        for (JsonNode n : node) {
            Operation op = new Operation(
                    UUID.fromString(n.get("id").asText()),
                    OperationType.valueOf(n.get("type").asText()),
                    UUID.fromString(n.get("bankAccountId").asText()),
                    new BigDecimal(n.get("amount").asText()),
                    LocalDate.parse(n.get("date").asText()),
                    n.get("description").asText(),
                    UUID.fromString(n.get("categoryId").asText()));
            if (n.has("active"))
                op.setActive(n.get("active").asBoolean());
            list.add(op);
        }

        return list;
    }
}

package com.example.guangzhouorder.dto;

import com.example.guangzhouorder.entity.ProductCard;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductCardDto {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Long productCardId;
    private String sku;
    private String name;
    private String material;
    private String configuration;
    private String stock;
    private String imageUrl;
    private String notes;
    private List<SpecEntry> specs;
    private BigDecimal displayPrice;
    private String categoryName;
    private boolean isPublic;
    private Long baseProductId;

    public ProductCardDto(ProductCard card) {
        this.productCardId = card.getProductCardId();
        this.sku = "GZ-" + card.getProductCardId();
        String rawName = card.getCardName();
        this.name = (rawName == null || rawName.isBlank()) ? "Unnamed Product" : rawName;
        this.displayPrice = card.getDisplayPrice();
        this.isPublic = card.isPublic();
        this.categoryName = card.getCategory() != null ? card.getCategory().getName() : "—";
        this.baseProductId = (card.getBaseProduct() != null) ? card.getBaseProduct().getBaseProductId() : null;
        this.imageUrl = null;
        this.stock = "—";
        this.material = "—";
        this.configuration = "—";
        this.notes = "";
        this.specs = new ArrayList<>();

        String dna = card.getCardDna();
        if (dna == null || dna.isBlank()) {
            return;
        }

        try {
            JsonNode root = OBJECT_MAPPER.readTree(dna);

            String dnaName = textOrNull(root.get("name"));
            if (dnaName != null && !dnaName.isBlank()) {
                this.name = dnaName;
            }

            String dnaImage = textOrNull(root.get("imageUrl"));
            if (dnaImage != null && !dnaImage.isBlank()) {
                this.imageUrl = dnaImage;
            }

            this.stock = textOrDefault(root.get("stock"), "—");
            this.material = textOrDefault(root.get("material"), "—");
            this.configuration = textOrDefault(root.get("configuration"), "—");
            this.notes = textOrDefault(root.get("notes"), "");

            JsonNode specsNode = root.get("specs");
            if (specsNode != null && specsNode.isArray()) {
                List<SpecEntry> parsedSpecs = new ArrayList<>();
                for (JsonNode specNode : specsNode) {
                    String label = textOrDefault(specNode.get("label"), "");
                    String value = textOrDefault(specNode.get("value"), "");
                    if (!label.isBlank()) {
                        parsedSpecs.add(new SpecEntry(label, value));
                    }
                }
                this.specs = parsedSpecs;
            }
        } catch (Exception ignored) {
            this.imageUrl = null;
            this.stock = "—";
            this.material = "—";
            this.configuration = "—";
            this.notes = "";
            this.specs = new ArrayList<>();
        }
    }

    private static String textOrNull(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        return node.asText();
    }

    private static String textOrDefault(JsonNode node, String defaultValue) {
        String value = textOrNull(node);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }

    @Data
    @NoArgsConstructor
    public static class SpecEntry {
        private String label;
        private String value;

        public SpecEntry(String label, String value) {
            this.label = label;
            this.value = value;
        }
    }
}

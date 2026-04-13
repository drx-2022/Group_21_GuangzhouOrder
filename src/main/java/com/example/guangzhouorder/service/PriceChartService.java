package com.example.guangzhouorder.service;

import com.example.guangzhouorder.entity.BaseProduct;
import com.example.guangzhouorder.entity.Category;
import com.example.guangzhouorder.entity.ProductCard;
import com.example.guangzhouorder.repository.BaseProductRepository;
import com.example.guangzhouorder.repository.CategoryRepository;
import com.example.guangzhouorder.repository.ProductCardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceChartService {

    private final ProductCardRepository productCardRepository;
    private final CategoryRepository categoryRepository;
    private final BaseProductRepository baseProductRepository;

    public record PriceChartDataPoint(
            Long productCardId,
            String date,
            BigDecimal price,
            String dnaName,
            String dnaMaterial,
            String imageUrl,
            String cardDna
    ) {}

    public List<PriceChartDataPoint> getChartData(Long categoryId) {
        List<ProductCard> cards;

        if (categoryId == null || categoryId == 0) {
            cards = productCardRepository.findByIsPublicTrueOrderByCreatedAtDesc();
        } else {
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isPresent()) {
                cards = productCardRepository.findByIsPublicTrueAndCategoryOrderByCreatedAtDesc(categoryOpt.get());
            } else {
                cards = productCardRepository.findByIsPublicTrueOrderByCreatedAtDesc();
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return cards.stream()
                .map(card -> {
                    String dna = card.getCardDna();
                    String name = extractJsonValue(dna, "name", "Unknown Product");
                    String material = extractJsonValue(dna, "material", "Unknown Material");
                    String imageUrl = extractJsonValue(dna, "imageUrl", "");
                    String createdAtStr = card.getCreatedAt() != null ? card.getCreatedAt().format(formatter) : "";

                    return new PriceChartDataPoint(
                            card.getProductCardId(),
                            createdAtStr,
                            card.getDisplayPrice() != null ? card.getDisplayPrice() : BigDecimal.ZERO,
                            name,
                            material,
                            imageUrl,
                            dna
                    );
                })
                .sorted(Comparator.comparing(PriceChartDataPoint::date))
                .collect(Collectors.toList());
    }

    public List<PriceChartDataPoint> getChartDataByBaseProduct(Long baseProductId) {
        BaseProduct baseProduct = baseProductRepository.findById(baseProductId)
                .orElseThrow(() -> new RuntimeException("BaseProduct not found"));

        List<ProductCard> cards = productCardRepository.findByBaseProductAndIsPublicTrueOrderByCreatedAtAsc(baseProduct);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return cards.stream()
                .map(card -> {
                    String dna = card.getCardDna();
                    String name = extractJsonValue(dna, "name", "Custom Order");
                    String material = extractJsonValue(dna, "material", "N/A");
                    String imageUrl = extractJsonValue(dna, "imageUrl", "");
                    String createdAtStr = card.getCreatedAt() != null ? card.getCreatedAt().format(formatter) : "";

                    return new PriceChartDataPoint(
                            card.getProductCardId(),
                            createdAtStr,
                            card.getDisplayPrice() != null ? card.getDisplayPrice() : BigDecimal.ZERO,
                            name,
                            material,
                            imageUrl,
                            dna
                    );
                })
                .collect(Collectors.toList());
    }

    private String extractJsonValue(String json, String key, String defaultValue) {
        if (json == null || json.isBlank()) {
            return defaultValue;
        }
        
        try {
            String searchKey = "\"" + key + "\"";
            int keyIndex = json.indexOf(searchKey);
            if (keyIndex == -1) {
                return defaultValue;
            }

            int valueStart = json.indexOf("\"", keyIndex + searchKey.length());
            if (valueStart == -1) {
                return defaultValue;
            }
            
            int valueEnd = json.indexOf("\"", valueStart + 1);
            if (valueEnd == -1) {
                return defaultValue;
            }

            return json.substring(valueStart + 1, valueEnd);
        } catch (Exception e) {
            log.error("Error parsing JSON key {} from string: {}", key, json, e);
            return defaultValue;
        }
    }
}

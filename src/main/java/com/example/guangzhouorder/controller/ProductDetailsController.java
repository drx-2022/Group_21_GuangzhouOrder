package com.example.guangzhouorder.controller;

import com.example.guangzhouorder.dto.ProductCardDto;
import com.example.guangzhouorder.entity.BaseProduct;
import com.example.guangzhouorder.entity.ProductCard;
import com.example.guangzhouorder.repository.ProductCardRepository;
import com.example.guangzhouorder.service.PriceChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class ProductDetailsController {

    private final ProductCardRepository productCardRepository;
    private final PriceChartService priceChartService;

    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        ProductCard card = productCardRepository.findById(id)
                .filter(c -> c.isPublic())
                .orElseThrow(() -> new IllegalArgumentException("Product not found or not public"));

        ProductCardDto cardDto = new ProductCardDto(card);
        model.addAttribute("card", cardDto);

        BaseProduct baseProduct = card.getBaseProduct();

        if (baseProduct != null) {
            model.addAttribute("chartData", priceChartService.getChartDataByBaseProduct(baseProduct.getBaseProductId()));
            model.addAttribute("baseProductName", baseProduct.getName());
        } else {
            model.addAttribute("chartData", Collections.emptyList());
            model.addAttribute("baseProductName", cardDto.getName());
        }

        return "product_details";
    }
}

package example.ms_stock.service;

import java.util.List;

import example.ms_stock.dto.StockRequestDTO;
import example.ms_stock.dto.StockResponseDTO;

public interface StockService {

    StockResponseDTO createStock(
            StockRequestDTO dto);

    List<StockResponseDTO> getAllStock();

    StockResponseDTO getStockById(Long id);

    StockResponseDTO getStockByProductId(
            Long productId);

    StockResponseDTO updateStock(
            Long id,
            StockRequestDTO dto);

    void deleteStock(Long id);
}
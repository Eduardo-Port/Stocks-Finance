package io.github.Eduardo_Port.stocksfinance.controller;

import io.github.Eduardo_Port.stocksfinance.domain.stock.Stock;
import io.github.Eduardo_Port.stocksfinance.domain.stock.dto.StockInputDTO;
import io.github.Eduardo_Port.stocksfinance.domain.stock.dto.StockUpdateInputDTO;
import io.github.Eduardo_Port.stocksfinance.services.StockService;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;
    private final PagedResourcesAssembler<Stock> pagedAssembler;

    public StockController(StockService stockService, PagedResourcesAssembler<Stock> pagedAssembler) {
        this.stockService = stockService;
        this.pagedAssembler = pagedAssembler;
    }

    @PostMapping
    public ResponseEntity<Stock> insert(@RequestBody StockInputDTO stockData) {
        Stock newStock = this.stockService.insert(stockData);
        return ResponseEntity.ok().body(newStock);
    }

    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<Stock>>> getStocksPaginated(@RequestParam(defaultValue = "0") int page) {
        Page<Stock> stocks = this.stockService.getStocksPaginated(page);
        PagedModel<EntityModel<Stock>> stocksPaginatedModel = pagedAssembler.toModel(stocks);
        return ResponseEntity.ok().body(stocksPaginatedModel);
    }

    @GetMapping("/")
    public ResponseEntity<Stock> getStockByTitle(@RequestParam String title) {
        Stock stock = this.stockService.getStockByTitle(title);
        return ResponseEntity.ok().body(stock);
    }

    @PutMapping()
    public ResponseEntity<Stock> update(@RequestBody StockInputDTO stockData) {
        Stock updatedStock = this.stockService.update(stockData);
        return ResponseEntity.ok().body(updatedStock);
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Stock> delete(@PathVariable("title") String title) {
        try {
            this.stockService.deleteByTitle(title);
        } catch (IllegalArgumentException as) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }


}

package com.kpmg.datamover;

import com.kpmg.datamover.destination.doa.DestinationProduct;
import com.kpmg.datamover.destination.doa.DestinationProductRepository;
import com.kpmg.datamover.source.dao.SourceProduct;
import com.kpmg.datamover.source.dao.SourceProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestResources {

    private final DestinationProductRepository destinationProductRepository;

    private final SourceProductRepository sourceProductRepository;

    private final TestService testService;

    public TestResources(DestinationProductRepository destinationProductRepository, SourceProductRepository sourceProductRepository, TestService testService) {
        this.destinationProductRepository = destinationProductRepository;
        this.sourceProductRepository = sourceProductRepository;
        this.testService = testService;
    }

    @GetMapping(path = "destination/product")
    public List<DestinationProduct> getDestinationProduct(){
        return destinationProductRepository.findAll();
    }

    @GetMapping(path = "source/product")
    public List<SourceProduct> getSourceProduct(){
        return sourceProductRepository.findAll();
    }


    @GetMapping(path = "add")
    public String addDate(){
        testService.addData();
        return "10 products added in source database";
    }

    @GetMapping(path = "move")
    public String moveData(){
        testService.moveData();
        return "Done";
    }
}

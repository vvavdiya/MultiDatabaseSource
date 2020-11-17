package com.kpmg.datamover;

import com.kpmg.datamover.destination.doa.DestinationProduct;
import com.kpmg.datamover.destination.doa.DestinationProductRepository;
import com.kpmg.datamover.source.dao.SourceProduct;
import com.kpmg.datamover.source.dao.SourceProductRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * To set up init data
 *
 */
@Service
public class TestService {

    private final SourceProductRepository sourceProductRepository;

    private final DestinationProductRepository destinationProductRepository;

    public TestService(SourceProductRepository productRepository, DestinationProductRepository destinationProductRepository) {
        this.sourceProductRepository = productRepository;
        this.destinationProductRepository = destinationProductRepository;
    }

    /**
     * This will insert dummy data in source database and after it will move data to destination
     * This
     *
     */
    public void addData(){
        System.out.println("===========adding 10 product in source database==========");
        for (int i=0;i<10;i++){
            SourceProduct sourceProduct = new SourceProduct();
            sourceProduct.setName(UUID.randomUUID().toString());
            sourceProductRepository.save(sourceProduct);
        }
        System.out.println("============10 product added in source database, now coping data in destination===========");
    }

    /**
     * Move data from source to destination
     *
     */
    public void moveData() {
        List<SourceProduct> sourceProducts = sourceProductRepository.findAll();
        List<DestinationProduct> destinationProducts = new ArrayList<>(sourceProducts.size());
        for (SourceProduct sourceProduct : sourceProducts) {
            DestinationProduct destinationProduct = new DestinationProduct();
            destinationProduct.setName(sourceProduct.getName());
            destinationProducts.add(destinationProduct);
        }
        destinationProductRepository.saveAll(destinationProducts);
    }
}

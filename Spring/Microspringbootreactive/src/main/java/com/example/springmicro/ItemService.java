package com.example.springmicro;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Flux<Item> getInventory() {
        return this.itemRepository.findAll();
    }

    public Mono<Item> saveItem(Item newItem) {
        return this.itemRepository.save(newItem);
    }

    public Mono<Void> deleteItem(String id) {
        return this.itemRepository.deleteById(id);
    }
}


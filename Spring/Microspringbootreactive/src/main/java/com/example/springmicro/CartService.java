package com.example.springmicro;

import java.util.stream.Collectors;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
class CartService {
    // write your code here for Task 7
    private ItemRepository itemRepository;

    private CartRepository cartRepository;

    CartService(ItemRepository repository,
                     CartRepository cartRepository) {
        this.itemRepository = repository;
        this.cartRepository = cartRepository;
    }

    public Mono<Cart> getCart(String cartId) {
        return this.cartRepository.findById(cartId);
    }

    public Flux<Item> getInventory() {
        return this.itemRepository.findAll();
    }

    Mono<Item> saveItem(Item newItem) {
        return this.itemRepository.save(newItem);
    }

    Mono<Void> deleteItem(String id) {
        return this.itemRepository.deleteById(id);
    }

    // write your code here for Task 8
 public Mono<Cart> addItemToCart(String cartId, String itemId) {
        return this.cartRepository.findById(cartId)
            .defaultIfEmpty(new Cart(cartId)) 
            .flatMap(cart -> cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                .findAny() 
                .map(cartItem -> {
                    cartItem.increment();
                    return Mono.just(cart);
                }) 
                .orElseGet(() -> {
                    return this.itemRepository.findById(itemId) 
                        .map(item -> new CartItem(item)) 
                        .map(cartItem -> {
                            cart.getCartItems().add(cartItem);
                            return cart;
                        });
                }))
            .flatMap(cart -> this.cartRepository.save(cart));
    }

    // write your code here for Task 9
    public Mono<Cart> removeOneFromCart(String cartId, String itemId) {
        return this.cartRepository.findById(cartId)
            .defaultIfEmpty(new Cart(cartId))
            .flatMap(cart -> cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                .findAny()
                .map(cartItem -> {
                    cartItem.decrement();
                    return Mono.just(cart);
                }) 
                .orElse(Mono.empty()))
            .map(cart -> new Cart(cart.getId(), cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getQuantity() > 0)
                .collect(Collectors.toList())))
            .flatMap(cart -> this.cartRepository.save(cart));
    }

    
}

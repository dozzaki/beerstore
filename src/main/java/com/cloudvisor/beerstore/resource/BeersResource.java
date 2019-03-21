package com.cloudvisor.beerstore.resource;

import com.cloudvisor.beerstore.model.Beer;
import com.cloudvisor.beerstore.repository.Beers;
import com.cloudvisor.beerstore.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/beers")
public class    BeersResource {

    @Autowired
    private BeerService beerService;

    @Autowired
    private Beers beers;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Beer create(@Valid @RequestBody Beer beer) {
        return beerService.save(beer);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable  Long id, @Valid @RequestBody Beer beer) {
        beer.setId(id);
        beerService.save(beer);
    }

    @GetMapping
    public List<Beer> all() {
        return beers.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        beerService.delete(id);
    }
}
package cz.czechitas.java2webapps.ukol7.controler;

import cz.czechitas.java2webapps.ukol7.entity.Vizitka;
import cz.czechitas.java2webapps.ukol7.repository.VizitkaRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class VizitkaControler {

    private final VizitkaRepository vizitkaRepository;

    public VizitkaControler(VizitkaRepository vizitkaRepository) {
        this.vizitkaRepository = vizitkaRepository;
    }

    @InitBinder
    public void nullStringBinding(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/")
    public ModelAndView seznam() {
        ModelAndView result = new ModelAndView("seznam");
        Iterable<Vizitka> listVizitek = vizitkaRepository.findAll();
        result.addObject("seznam", listVizitek);
        return result;
    }

    @GetMapping("/{id:[0-9]+}")
    public Object vizitka(@PathVariable Integer id) {
        Optional<Vizitka> vizitka = vizitkaRepository.findById(id);

        if (vizitka.isPresent()) {
            ModelAndView result = new ModelAndView("vizitka");
            result.addObject("vizitka", vizitka.get());
            return result;
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/nova")
    public ModelAndView nova() {
        ModelAndView result = new ModelAndView("formular");
        result.addObject("vizitka", new Vizitka());
        return result;
    }

    @PostMapping(value = "/nova")
    public Object vizitka(@ModelAttribute("vizitka") @Valid Vizitka vizitka, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "formular";
        }

        vizitka.setId(null);
        vizitkaRepository.save(vizitka);
        return "redirect:/";
    }

    @PostMapping(value = "/{id:[0-9]+}")
    public Object smazat(@PathVariable Integer id) {
        Optional<Vizitka> vizitka = vizitkaRepository.findById(id);
        vizitkaRepository.delete(vizitka.get());
        return "redirect:/";
    }

    @GetMapping(value = "/upravit/{id:[0-9]+}")
    public Object upravit(@PathVariable Integer id) {
        Optional<Vizitka> vizitka = vizitkaRepository.findById(id);
        ModelAndView oprava = new ModelAndView("formularProUpravu");
        oprava.addObject("vizitka",vizitka.get());
        return oprava;
    }


    @PostMapping(value = "/uprava/{id:[0-9]+}")
    public Object upravit(@PathVariable Integer id,@ModelAttribute("vizitka") @Valid Vizitka vizitka, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "formularProUpravu";
        }
        vizitka.setId(id);
        vizitkaRepository.save(vizitka);
        return "redirect:/";
    }

}

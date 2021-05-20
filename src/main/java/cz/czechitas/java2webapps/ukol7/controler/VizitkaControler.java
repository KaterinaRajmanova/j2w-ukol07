package cz.czechitas.java2webapps.ukol7.controler;

import cz.czechitas.java2webapps.ukol7.entity.Vizitka;
import cz.czechitas.java2webapps.ukol7.repository.VizitkaRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
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
    public ModelAndView seznam (){
        ModelAndView result = new ModelAndView("seznam");
        Iterable<Vizitka> listVizitek = vizitkaRepository.findAll();
        result.addObject("seznam",listVizitek);
        return result;
    }

    @GetMapping("/{id:[0-9]+}")
    public Object vizitka(@PathVariable Integer id){
        Optional<Vizitka> vizitka = vizitkaRepository.findById(id);

        if (vizitka.isPresent()) {
            ModelAndView result = new ModelAndView("vizitka");
            result.addObject("vizitka", vizitka.get());
            return result;
        }
        return ResponseEntity.notFound().build();
    }

}

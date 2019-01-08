package ch.moviescore.core.controller;

import ch.moviescore.core.model.ServiceStatusModel;
import ch.moviescore.core.service.AvalibleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("status")
public class WidgetController {

    private AvalibleService avalibleService;

    public WidgetController(AvalibleService avalibleService) {
        this.avalibleService = avalibleService;
    }

    @GetMapping(produces = "application/json")
    public List<ServiceStatusModel> getServerStatus() {
        List<ServiceStatusModel> serviceStatusModels = new ArrayList<>();
        serviceStatusModels.add(new ServiceStatusModel(
                "minecraft", avalibleService.checkOnline("scorewinner.ch", 25565)));
        serviceStatusModels.add(new ServiceStatusModel(
                "steam", avalibleService.checkOnline("scorewinner.ch", 7777)));
        serviceStatusModels.add(new ServiceStatusModel(
                "pterodactyl", avalibleService.checkOnline("games.scorewinner.ch", 80)));
        serviceStatusModels.add(new ServiceStatusModel(
                "moviedb", avalibleService.checkOnline("movie.scorewinner.ch", 80)));
        serviceStatusModels.add(new ServiceStatusModel(
                "hermann", avalibleService.checkOnline("scorewinner.ch", 8090)));
        return serviceStatusModels;
    }
}

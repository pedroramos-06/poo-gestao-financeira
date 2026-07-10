package WalletFlow.sistemagestaofinanceira.controllers;

import WalletFlow.sistemagestaofinanceira.dto.DashboardDTO;
import WalletFlow.sistemagestaofinanceira.models.Usuario;
import WalletFlow.sistemagestaofinanceira.service.DashboardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.YearMonth;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String dashboard(
            @RequestParam(required = false) YearMonth periodo,
            Model model,
            @AuthenticationPrincipal Usuario usuario) {

        if (periodo == null) {
            periodo = YearMonth.now();
        }

        try{
            DashboardDTO dashboard = dashboardService.getResumo(usuario.getId(), periodo);

            model.addAttribute("periodoSelecionado", periodo);
            model.addAttribute("dashboard", dashboard);
            return "dashboard/dashboard";
        }  catch (Exception e) {
            model.addAttribute("erro", "Erro ao carregar dashboard");
            return "dashboard/dashboard";
        }
    }

}

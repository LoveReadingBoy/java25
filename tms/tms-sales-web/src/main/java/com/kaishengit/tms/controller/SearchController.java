package com.kaishengit.tms.controller;

import com.kaishengit.tms.entity.Ticket;
import com.kaishengit.tms.entity.TicketStore;
import com.kaishengit.tms.service.TicketService;
import com.kaishengit.tms.shiro.ShiroUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 查询控制器
 * @author fankay
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private ShiroUtil shiroUtil;

    @GetMapping("/ticket")
    public String searchTicket(@RequestParam(required = false,defaultValue = "") String ticketNum,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if(StringUtils.isNotEmpty(ticketNum)) {
            Ticket ticket = ticketService.findTicketByTicketNum(ticketNum);
            if(ticket == null) {
                redirectAttributes.addFlashAttribute("message","该年票不存在");
            } else {
                TicketStore ticketStore = shiroUtil.getCurrentAccount();
                if(ticket.getStoreAccountId().equals(ticketStore.getId())) {
                    model.addAttribute("ticket",ticket);
                } else {
                    redirectAttributes.addFlashAttribute("message","该年票不属于该售票点，查询失败");
                }
            }
        }
        return "search/ticket";
    }
}

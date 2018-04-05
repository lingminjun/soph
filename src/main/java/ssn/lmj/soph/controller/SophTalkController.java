package ssn.lmj.soph.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ssn.lmj.soph.service.SophTalkService;
import ssn.lmj.soph.service.entities.Dialog;


@RestController // all api
public class SophTalkController {

    @Autowired
    private SophTalkService sophTalkService;

    @RequestMapping(value = "/api/say", method = RequestMethod.GET)
    public Dialog say(@RequestParam(value = "text", required = true) String text) {
        return sophTalkService.say(text);
    }

    @RequestMapping(value = "/api/tell", method = RequestMethod.POST)
    public void tell(@RequestBody Dialog dialog) {
        sophTalkService.tell(dialog);
    }

}

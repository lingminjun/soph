package ssn.lmj.soph.service;


import ssn.lmj.soph.service.entities.Dialog;

public interface SophTalkService {
    Dialog say(String text);
    boolean tell(Dialog dialog);
}

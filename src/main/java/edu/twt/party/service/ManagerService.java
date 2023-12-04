package edu.twt.party.service;

import edu.twt.party.helper.ResponseHelper;
import edu.twt.party.pojo.manager.AuthorizeReq;
import edu.twt.party.pojo.user.AcntPass;
import ognl.Token;

public interface ManagerService extends BaseService{

    String login(AcntPass acntPass);

    Boolean authorize(AuthorizeReq authorizeReq, String token);
}

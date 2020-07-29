package io.nuite.modules.system.from;

import io.nuite.modules.sr_base.entity.BaseUserEntity;

import java.util.List;

public class SubAccountForm {
    private BaseUserEntity account;
    private List<Long> menu;

    public BaseUserEntity getAccount() {
        return account;
    }

    public void setAccount(BaseUserEntity account) {
        this.account = account;
    }

    public List<Long> getMenu() {
        return menu;
    }

    public void setMenu(List<Long> menu) {
        this.menu = menu;
    }
}

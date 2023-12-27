package dev.erikmota.desafiounikamain;

import dev.erikmota.desafiounikamain.view.DashboardPage;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class WicketApplication extends WebApplication {
    public Class<? extends Page> getHomePage() {
        return DashboardPage.class;
    }

    public void init()
    {
        super.init();
    }
}

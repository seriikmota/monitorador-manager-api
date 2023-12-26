package dev.erikmota;

import org.apache.wicket.markup.html.link.Link;
public class DashboardPage extends BasePage {
    public DashboardPage() {
        add(new Link<Void>("hrefMonitorador") {
            @Override
            public void onClick() {
                setResponsePage(MonitoradorPage.class);
            }
        });

        add(new Link<Void>("hrefEndereco") {
            @Override
            public void onClick() {
                setResponsePage(EnderecoPage.class);
            }
        });
    }
}

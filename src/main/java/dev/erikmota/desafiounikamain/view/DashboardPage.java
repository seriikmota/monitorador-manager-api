package dev.erikmota.desafiounikamain.view;

import dev.erikmota.desafiounikamain.view.modelsView.Endereco;
import dev.erikmota.desafiounikamain.view.modelsView.Monitorador;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public class DashboardPage extends BasePage {
    public DashboardPage() {
        monitoradorList = atualizarLista("http://localhost:8081/monitorador", Monitorador.class);
        enderecoList = atualizarLista("http://localhost:8081/endereco", Endereco.class); MapEndereco();

        add(new Label("quantMonitorador", monitoradorList.size()));
        add(new Label("quantEndereco", enderecoList.size()));

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

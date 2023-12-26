package dev.erikmota.desafiounikamain.view;

import dev.erikmota.models.Monitorador;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import java.util.List;


public class MonitoradorPage extends BasePage {

    public MonitoradorPage() {

        List<Monitorador> monitoradorList = atualizarListar("http://localhost:8081/monitorador", Monitorador.class);
        try {
            add(new ListView<>("monitoradorList", monitoradorList) {
                @Override
                protected void populateItem(ListItem<Monitorador> item) {
                    final Monitorador monitorador = item.getModelObject();
                    item.add(new Label("mId", monitorador.getId()));
                    item.add(new Label("mTipoPessoa", monitorador.getTipoPessoa()));
                    item.add(new Label("mCnpj", monitorador.getCnpj()));
                    item.add(new Label("mRazaoSocial", monitorador.getRazaoSocial()));
                    item.add(new Label("mCpf", monitorador.getCpf()));
                    item.add(new Label("mNome", monitorador.getNome()));
                    item.add(new Label("mRg", monitorador.getRg()));
                    item.add(new Label("mInscricaoSocial", monitorador.getInscricaoSocial()));
                    item.add(new Label("mEmail", monitorador.getEmail()));
                    item.add(new Label("mDataNascimento", monitorador.getDataNascimento()));
                    item.add(new Label("mQuantidadeEndereco", monitorador.getEnderecos().size()));
                    item.add(new Label("mAtivo", monitorador.getAtivo()));
                }
            });
        } catch (NullPointerException e){
            System.out.println("NullPointerException");
        }

        add(new Link<Void>("hrefDashboard") {
            @Override
            public void onClick() {
                setResponsePage(DashboardPage.class);
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

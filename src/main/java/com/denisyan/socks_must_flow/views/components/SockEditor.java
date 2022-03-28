package com.denisyan.socks_must_flow.views.components;

import com.denisyan.socks_must_flow.entity.Sock;
import com.denisyan.socks_must_flow.service.SocksViewService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class SockEditor extends VerticalLayout implements KeyNotifier {

    private final SocksViewService socksViewService;
    //todo 1.надо поменять на ДТО и ДТО сделать все поля строками
    private Sock sock;
    TextField color = new TextField("Color");
    TextField cottonPart = new TextField("Cotton Part");
    TextField quantity = new TextField("Quantity");
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    Binder<Sock> binder = new Binder<>(Sock.class);
    @Setter
    ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }
    @Autowired
    public SockEditor(SocksViewService socksViewService) {
        this.socksViewService = socksViewService;

        add(color, cottonPart, quantity, actions);
        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editSock(sock));
        setVisible(false);

    }


    public void save() {
        socksViewService.update(sock);
        changeHandler.onChange();
    }

    public void delete() {
        socksViewService.delete(sock);
        changeHandler.onChange();
    }

    public void editSock(Sock newSock) {
        if (newSock == null) {
            setVisible(false);
            return;
        }

        if (newSock.getId() != null) {
            sock = socksViewService.findById(newSock.getId()).orElse(newSock);
        } else {
            sock = newSock;
        }
        binder.setBean(sock);

        setVisible(true);

        color.focus();


    }
}

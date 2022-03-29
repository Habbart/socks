package com.denisyan.socks_must_flow.views.components;

import com.denisyan.socks_must_flow.entity.Sock;
import com.denisyan.socks_must_flow.service.SocksService;
import com.denisyan.socks_must_flow.validators.color_validator.AllowedColors;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class SockEditor extends VerticalLayout implements KeyNotifier {

    private final transient SocksService socksService;

    private transient Sock sock;
    ComboBox<String> color = new ComboBox<>("Color");
    TextField cottonPart = new TextField("Cotton Part");
    TextField quantity = new TextField("Quantity");
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    Binder<Sock> binder = new Binder<>(Sock.class);
    @Setter
    transient ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public SockEditor(SocksService socksService) {
        this.socksService = socksService;
        color.setItems(AllowedColors.getAllowedColorList());
        add(color, cottonPart, quantity, actions);

        setUpBinder();

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
        socksService.addOrUpdateSocks(sock);
        changeHandler.onChange();
    }

    public void delete() {
        socksService.delete(sock);
        changeHandler.onChange();
    }

    public void editSock(Sock newSock) {
        if (newSock == null) {
            setVisible(false);
            return;
        }

        if (newSock.getId() != null) {
            sock = socksService.findById(newSock.getId()).orElse(newSock);
        } else {
            sock = newSock;
        }
        binder.setBean(sock);

        setVisible(true);

        color.focus();
    }

    //из-за бага в Vaadin надо в ручную прописывать конвертер из строк в числа
    // https://vaadin.com/forum/thread/15385912/binding-from-integer-not-working
    private void setUpBinder() {
        binder.forField(this.cottonPart)
                .withNullRepresentation("")
                .withConverter(new StringToIntegerConverter(0, "integers only"))
                .bind(Sock::getCottonPart, Sock::setCottonPart);

        binder.forField(this.quantity)
                .withNullRepresentation("")
                .withConverter(new StringToIntegerConverter(0, "integers only"))
                .bind(Sock::getQuantity, Sock::setQuantity);
    }
}

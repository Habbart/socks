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
    Button deletePosition = new Button("Delete position", VaadinIcon.TRASH.create());
    Button setToZero = new Button("Set to zero", VaadinIcon.HAMMER.create());
    //делим основные действия на два Layout, чтобы можно было убирать кнопки удаления
    HorizontalLayout actions = new HorizontalLayout(save, cancel);
    HorizontalLayout deleteActions = new HorizontalLayout(deletePosition, setToZero);
    Binder<Sock> binder = new Binder<>(Sock.class);
    @Setter
    transient ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public SockEditor(SocksService socksService) {
        this.socksService = socksService;
        //берём список доступных цветов из Енума доступных цветов
        color.setItems(AllowedColors.getAllowedColorList());
        //устанавливаем все компоненты
        add(color, cottonPart, quantity, actions, deleteActions);
        //из-за бага надо в ручную прописывать конвертер строк - подробности в методе
        setUpBinder();
        //биндим поля этого класса
        binder.bindInstanceFields(this);
        setSpacing(true);

        //меняем цвет кнопок
        save.getElement().getThemeList().add("primary");
        deletePosition.getElement().getThemeList().add("error");
        setToZero.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());
        // устанавливаем листенеры и методы на кнопки
        save.addClickListener(e -> save());
        cancel.addClickListener(e -> cancelView());
        deletePosition.addClickListener(e -> removePosition());
        setToZero.addClickListener(e -> resetToZero());

        setVisible(false);

    }

    //убираем вьюху при нажатии на кнопку отмена
    private void cancelView() {
        setVisible(false);
        changeHandler.onChange();
    }

    public void save() {
        socksService.addOrUpdateSocks(sock);
        changeHandler.onChange();
    }

    public void removePosition() {
        socksService.delete(sock);
        changeHandler.onChange();
    }

    //обнуляем количество до нуля
    public void resetToZero() {
        socksService.restRemoveSocks(sock);
        changeHandler.onChange();
    }

    public void editSock(Sock newSock) {
        if (newSock == null) {
            setVisible(false);
            return;
        }
        //убираем кнопки удаления позиции и обнуления позиции
        deleteActions.setVisible(false);
        //даем возможность редактировать цвет и хлопковую часть
        color.setReadOnly(false);
        cottonPart.setReadOnly(false);

        if (newSock.getId() != null) {
            sock = socksService.findById(newSock.getId()).orElse(newSock);
        } else {
            sock = newSock;
        }
        binder.setBean(sock);

        setVisible(true);

        color.focus();
    }

    //убираем возможность редактировать цвет и хлопок, а также добавляем кнопки удаления и обнуления
    public void setColorAndCottonNonActive() {
        color.setReadOnly(true);
        cottonPart.setReadOnly(true);
        deleteActions.setVisible(true);

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

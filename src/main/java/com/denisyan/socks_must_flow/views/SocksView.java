package com.denisyan.socks_must_flow.views;


import com.denisyan.socks_must_flow.entity.Sock;
import com.denisyan.socks_must_flow.service.SocksService;
import com.denisyan.socks_must_flow.views.components.SockEditor;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route("/")
public class SocksView extends VerticalLayout {

    private final transient SocksService socksService;
    private final Button addNewButton;
    final TextField filter = new TextField();
    private Grid<Sock> grid = new Grid<>(Sock.class);
    private SockEditor sockEditor;

    public SocksView(SocksService socksService, SockEditor sockEditor) {
        this.socksService = socksService;
        this.sockEditor = sockEditor;
        this.addNewButton = new Button("Add sock", VaadinIcon.PLUS.create());
        setFilter();

        add(    createHeader(),
                getAddButtonAndFilterAndGridLayout()
                );
        setSizeFull();
        // Instantiate and edit new Customer the new button is clicked
        addNewButton.addClickListener(e -> sockEditor.editSock(new Sock()));

        // Listen changes made by the editor, refresh data from backend
        sockEditor.setChangeHandler(() -> {
            sockEditor.setVisible(false);
            filterSocks(filter.getValue());
        });

        // Initialize listing
        filterSocks("");

    }
    //создаем Header для кнопки выхода
    private Component createHeader() {
        H1 logo = new H1("Socks must flow");
        logo.addClassName("logo");
        Anchor logout = new Anchor("/logout", "Log out");
        HorizontalLayout header = new HorizontalLayout(logo, logout);
        header.setWidth("100%");
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.setVisible(true);
        return header;
    }
    //устанавливаем кнопки добавить носки, фильтр и список носков в один Layout на одном уровне
    private Component getAddButtonAndFilterAndGridLayout(){
        VerticalLayout buttonAndFilter = new VerticalLayout(addNewButton, filter, sockEditor);
        buttonAndFilter.setWidth("20%");
        buttonAndFilter.setHeight("20%");
        buttonAndFilter.setHorizontalComponentAlignment(Alignment.CENTER);
        setGrid();
        HorizontalLayout horizontalLayout = new HorizontalLayout(buttonAndFilter, grid);
        horizontalLayout.setAlignItems(Alignment.START);
        horizontalLayout.setSizeFull();
        return horizontalLayout;
    }
    //ставим настройки фильтра
    private void setFilter(){
        filter.setPlaceholder("Filter by color");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> filterSocks(e.getValue()));
    }
    //ставим настройки вывода носков
    private void setGrid(){
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(e -> {
            sockEditor.editSock(e.getValue());
            sockEditor.setColorAndCottonNonActive();
        });
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    public void filterSocks(String color) {
        if (color.isEmpty()) {
            grid.setItems(socksService.findAll());
        } else {
            grid.setItems(socksService.findByColor(color));
        }
    }
}
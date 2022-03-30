package com.denisyan.socks_must_flow.views;


import com.denisyan.socks_must_flow.entity.Sock;
import com.denisyan.socks_must_flow.service.SocksService;
import com.denisyan.socks_must_flow.views.components.SockEditor;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
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
    final TextField filter;
    private Grid<Sock> grid;


    public SocksView(SocksService socksService, SockEditor sockEditor) {
        this.socksService = socksService;
        this.addNewButton = new Button("Add sock", VaadinIcon.PLUS.create());

        filter = new TextField();
        grid = new Grid<>(Sock.class);

        HorizontalLayout actions = new HorizontalLayout(filter, addNewButton);

        add(actions, grid, sockEditor);

        filter.setPlaceholder("Filter by color");

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> filterSocks(e.getValue()));

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            sockEditor.editSock(e.getValue());
            sockEditor.setColorAndCottonNonActive();
        });
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

    public void filterSocks(String color) {
        if (color.isEmpty()) {
            grid.setItems(socksService.findAll());
        } else {
            grid.setItems(socksService.findByColor(color));
        }
    }
}
//package com.example.application.views.helloworld;
//
//import com.denisyan.socks_must_flow.entity.Sock;
//import com.denisyan.socks_must_flow.service.SocksService;
//import com.denisyan.socks_must_flow.views.MainLayout;
//import com.example.application.views.MainLayout;
//import com.vaadin.flow.component.Component;
//import com.vaadin.flow.component.HasStyle;
//import com.vaadin.flow.component.UI;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.checkbox.Checkbox;
//import com.vaadin.flow.component.datepicker.DatePicker;
//import com.vaadin.flow.component.dependency.Uses;
//import com.vaadin.flow.component.formlayout.FormLayout;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.grid.GridVariant;
//import com.vaadin.flow.component.html.Div;
//import com.vaadin.flow.component.icon.Icon;
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.splitlayout.SplitLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.data.binder.BeanValidationBinder;
//import com.vaadin.flow.data.binder.ValidationException;
//import com.vaadin.flow.data.renderer.TemplateRenderer;
//import com.vaadin.flow.router.BeforeEnterEvent;
//import com.vaadin.flow.router.BeforeEnterObserver;
//import com.vaadin.flow.router.PageTitle;
//import com.vaadin.flow.router.Route;
//import java.util.Optional;
//import javax.annotation.security.PermitAll;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//
//@PageTitle("Hello World")
//@Route(value = "stock", layout = MainLayout.class)
//@PermitAll
//@Uses(Icon.class)
//public class StockOfSocks extends Div implements BeforeEnterObserver {
//
//    private final String SAMPLEPERSON_ID = "samplePersonID";
//    private final String SAMPLEPERSON_EDIT_ROUTE_TEMPLATE = "hello/%d/edit";
//
//    private Grid<Sock> grid = new Grid<>(Sock.class, false);
//
//    private TextField firstName;
//    private TextField lastName;
//    private TextField email;
//    private TextField phone;
//    private DatePicker dateOfBirth;
//    private TextField occupation;
//    private Checkbox important;
//
//    private Button cancel = new Button("Cancel");
//    private Button save = new Button("Save");
//
//    private BeanValidationBinder<Sock> binder;
//
//    private Sock sock;
//
//    private SocksService socksService;
//
//    public StockOfSocks(@Autowired SocksService socksService) {
//        this.socksService = socksService;
//        addClassNames("stock-of-socks-view", "flex", "flex-col", "h-full");
//
//        // Create UI
//        SplitLayout splitLayout = new SplitLayout();
//        splitLayout.setSizeFull();
//
//        createGridLayout(splitLayout);
//        createEditorLayout(splitLayout);
//
//        add(splitLayout);
//
//        // Configure Grid
//        grid.addColumn("firstName").setAutoWidth(true);
//        grid.addColumn("lastName").setAutoWidth(true);
//        grid.addColumn("email").setAutoWidth(true);
//        grid.addColumn("phone").setAutoWidth(true);
//        grid.addColumn("dateOfBirth").setAutoWidth(true);
//        grid.addColumn("occupation").setAutoWidth(true);
//        TemplateRenderer<Sock> importantRenderer = TemplateRenderer.<Sock>of(
//                        "<vaadin-icon hidden='[[!item.important]]' icon='vaadin:check' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-primary-text-color);'></vaadin-icon><vaadin-icon hidden='[[item.important]]' icon='vaadin:minus' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: var(--lumo-disabled-text-color);'></vaadin-icon>")
//                .withProperty("important", Sock::isImportant);
//        grid.addColumn(importantRenderer).setHeader("Important").setAutoWidth(true);
//
//        grid.setItems(query -> socksService.list(
//                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
//                .stream());
//        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
//        grid.setHeightFull();
//
//        // when a row is selected or deselected, populate form
//        grid.asSingleSelect().addValueChangeListener(event -> {
//            if (event.getValue() != null) {
//                UI.getCurrent().navigate(String.format(SAMPLEPERSON_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
//            } else {
//                clearForm();
//                UI.getCurrent().navigate(com.example.application.views.helloworld.StockOfSocks.class);
//            }
//        });
//
//        // Configure Form
//        binder = new BeanValidationBinder<>(Sock.class);
//
//        // Bind fields. This where you'd define e.g. validation rules
//
//        binder.bindInstanceFields(this);
//
//        cancel.addClickListener(e -> {
//            clearForm();
//            refreshGrid();
//        });
//
//        save.addClickListener(e -> {
//            try {
//                if (this.sock == null) {
//                    this.sock = new Sock();
//                }
//                binder.writeBean(this.sock);
//
//                socksService.update(this.sock);
//                clearForm();
//                refreshGrid();
//                Notification.show("SamplePerson details stored.");
//                UI.getCurrent().navigate(com.example.application.views.helloworld.StockOfSocks.class);
//            } catch (ValidationException validationException) {
//                Notification.show("An exception happened while trying to store the samplePerson details.");
//            }
//        });
//
//    }
//
//    @Override
//    public void beforeEnter(BeforeEnterEvent event) {
//        Optional<Integer> samplePersonId = event.getRouteParameters().getInteger(SAMPLEPERSON_ID);
//        if (samplePersonId.isPresent()) {
//            Optional<Sock> samplePersonFromBackend = samplePersonService.get(samplePersonId.get());
//            if (samplePersonFromBackend.isPresent()) {
//                populateForm(samplePersonFromBackend.get());
//            } else {
//                Notification.show(
//                        String.format("The requested samplePerson was not found, ID = %d", samplePersonId.get()), 3000,
//                        Notification.Position.BOTTOM_START);
//                // when a row is selected but the data is no longer available,
//                // refresh grid
//                refreshGrid();
//                event.forwardTo(com.example.application.views.helloworld.StockOfSocks.class);
//            }
//        }
//    }
//
//    private void createEditorLayout(SplitLayout splitLayout) {
//        Div editorLayoutDiv = new Div();
//        editorLayoutDiv.setClassName("flex flex-col");
//        editorLayoutDiv.setWidth("400px");
//
//        Div editorDiv = new Div();
//        editorDiv.setClassName("p-l flex-grow");
//        editorLayoutDiv.add(editorDiv);
//
//        FormLayout formLayout = new FormLayout();
//        firstName = new TextField("First Name");
//        lastName = new TextField("Last Name");
//        email = new TextField("Email");
//        phone = new TextField("Phone");
//        dateOfBirth = new DatePicker("Date Of Birth");
//        occupation = new TextField("Occupation");
//        important = new Checkbox("Important");
//        important.getStyle().set("padding-top", "var(--lumo-space-m)");
//        Component[] fields = new Component[]{firstName, lastName, email, phone, dateOfBirth, occupation, important};
//
//        for (Component field : fields) {
//            ((HasStyle) field).addClassName("full-width");
//        }
//        formLayout.add(fields);
//        editorDiv.add(formLayout);
//        createButtonLayout(editorLayoutDiv);
//
//        splitLayout.addToSecondary(editorLayoutDiv);
//    }
//
//    private void createButtonLayout(Div editorLayoutDiv) {
//        HorizontalLayout buttonLayout = new HorizontalLayout();
//        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
//        buttonLayout.setSpacing(true);
//        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        buttonLayout.add(save, cancel);
//        editorLayoutDiv.add(buttonLayout);
//    }
//
//    private void createGridLayout(SplitLayout splitLayout) {
//        Div wrapper = new Div();
//        wrapper.setId("grid-wrapper");
//        wrapper.setWidthFull();
//        splitLayout.addToPrimary(wrapper);
//        wrapper.add(grid);
//    }
//
//    private void refreshGrid() {
//        grid.select(null);
//        grid.getLazyDataView().refreshAll();
//    }
//
//    private void clearForm() {
//        populateForm(null);
//    }
//
//    private void populateForm(SamplePerson value) {
//        this.sock = value;
//        binder.readBean(this.sock);
//
//    }
//}
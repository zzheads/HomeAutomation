package com.zzheads.HomeAutomation.model;//

import com.zzheads.HomeAutomation.controller.RoomController;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

// HomeAutomation
// com.zzheads.HomeAutomation.model created by zzheads on 20.08.2016.
//
@Entity
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    private List<Control> controls;
    @ManyToOne
    private Room room;

    public Equipment() {
    }

    // JSON constructor
    public Equipment(Map<String, String> map) {
        name = map.get("equipmentName");
    }

    public Equipment(String name, Room room, List<Control> controls) {
        this.name = name;
        this.room = room;
        this.controls = controls;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Control> getControls() {
        return controls;
    }

    public void setControls(List<Control> controls) {
        this.controls = controls;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @SuppressWarnings("unchecked")
    private List<Map> getLinks() {
        List<Map> links = new ArrayList<>();
        final String parentString = RoomController.BASE_URL+"room/"+room.getId()+"/equipment";
        final String selfString = parentString + "/"+id;
        final String controlString = selfString + "/control";

        links.add(new HashMap<>());
        links.get(0).put("rel", "self");
        links.get(0).put("href", selfString);
        links.add(new HashMap<>());
        links.get(1).put("rel", "parent");
        links.get(1).put("href", parentString);
        links.add(new HashMap<>());
        links.get(2).put("rel", "control");
        links.get(2).put("href", controlString);

        return links;
    }

    public Map Json() {
        Map <String, Object> res = new HashMap<>();
        res.put("equipmentId", String.valueOf(id));
        res.put("equipmentName", name);
        res.put("_links", getLinks());

        return res;
    }

    @SuppressWarnings("unchecked")
    public static List Json(List<Equipment> equipments) {
        List<Map> res = new ArrayList<>();

        for (int i=0; i<equipments.size(); i++) {
            res.add(new HashMap<>());
            res.get(i).put("equipmentId", String.valueOf(equipments.get(i).getId()));
            res.get(i).put("equipmentName", equipments.get(i).getName());
            res.get(i).put("_links", equipments.get(i).getLinks());
        }
        return res;
    }

    public void addControl(Control control) {
        controls.add(control);
    }

    public void removeControl(Control control) {
        for (int i=0;i<controls.size();i++) {
            if (Objects.equals(controls.get(i).getId(), control.getId())) controls.remove(i);
        }
    }

}

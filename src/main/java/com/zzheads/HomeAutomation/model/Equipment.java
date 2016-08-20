package com.zzheads.HomeAutomation.model;//

import com.zzheads.HomeAutomation.controller.RoomController;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// HomeAutomation
// com.zzheads.HomeAutomation.model created by zzheads on 20.08.2016.
//
@Entity
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "equipment", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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

    private Map[] getLinks() {
        Map <String, String> linksSelf = new HashMap<>();
        linksSelf.put("rel", "self");
        linksSelf.put("href", RoomController.BASE_URL+"room/"+room.getId()+"/equipment/"+id);
        Map <String, String> linksParent = new HashMap<>();
        linksParent.put("rel", "parent");
        linksParent.put("href", RoomController.BASE_URL+"room/"+room.getId());
        Map <String, String> linksControl = new HashMap<>();
        linksControl.put("rel", "control");
        linksControl.put("href", RoomController.BASE_URL+"room/"+room.getId()+"/equipment/"+id+"/control");
        Map[] links = new Map[3];
        links[0] = linksSelf;
        links[1] = linksParent;
        links[2] = linksControl;
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

}

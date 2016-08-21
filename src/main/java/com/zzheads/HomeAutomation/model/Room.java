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
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int squareFootage;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Equipment> equipments = new ArrayList<>();

    public Room() {
    }

    // JSON constructor
    public Room(Map<String, String> map) {
        name = map.get("roomName");
        squareFootage = Integer.parseInt(map.get("squareFootage"));
    }

    public Room(String name, int squareFootage) {
        this.name = name;
        this.squareFootage = squareFootage;
    }

    public Room(String name, int squareFootage, List<Equipment> equipments) {
        this.name = name;
        this.squareFootage = squareFootage;
        this.equipments = equipments;
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

    public int getSquareFootage() {
        return squareFootage;
    }

    public void setSquareFootage(int squareFootage) {
        this.squareFootage = squareFootage;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public void addEquipment(Equipment equipment) {
        equipments.add(equipment);
    }

    public void removeEquipment(Equipment equipment) {
        for (int i=0;i<equipments.size();i++) {
            if (Objects.equals(equipments.get(i).getId(), equipment.getId())) equipments.remove(i);
        }
    }

    private Map[] getLinks(){
        Map <String, String> linksSelf = new HashMap<>();
        linksSelf.put("rel", "self");
        linksSelf.put("href", RoomController.BASE_URL+"room/"+id);
        Map <String, String> linksEquipment = new HashMap<>();
        linksEquipment.put("rel", "equipment");
        linksEquipment.put("href", RoomController.BASE_URL+"room/"+id+"/equipment");
        Map[] links = new Map[2];
        links[0] = linksSelf;
        links[1] = linksEquipment;
        return links;
    }

    public Map Json() {
        Map <String, Object> res = new HashMap<>();
        res.put("roomId", String.valueOf(id));
        res.put("roomName", name);
        res.put("squareFootage", String.valueOf(squareFootage));
        res.put("_links", getLinks());

        return res;
    }

    public static List Json(List<Room> rooms) {
        List<Map> res = new ArrayList<>();

        for (int i=0; i<rooms.size(); i++) {
            res.add(new HashMap<>());
            res.get(i).put("roomId", String.valueOf(rooms.get(i).getId()));
            res.get(i).put("roomName", rooms.get(i).getName());
            res.get(i).put("squareFootage", String.valueOf(rooms.get(i).getSquareFootage()));
            res.get(i).put("_links", rooms.get(i).getLinks());
        }
        return res;
    }
}

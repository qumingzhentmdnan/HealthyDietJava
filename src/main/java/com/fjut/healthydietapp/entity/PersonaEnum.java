package com.fjut.healthydietapp.entity;

import lombok.Data;


public enum PersonaEnum {
    NingGuang("你是原神中的一个角色，璃月七星中的天权星凝光，沉稳，言语简洁，对人称呼很正式，不喜欢多说废话。接下来你将帮助我了解一些饮食方面的事情。",0,"凝光"),
    NaWeiLaiTe("你是原神中的一个角色，枫丹的最高审判官水龙王那维莱特，沉稳，言语简洁，对人称呼很正式，不喜欢多说废话，接下来你将帮助我了解一些饮食方面的事情。",1,"那维莱特"),
    BaChongShengZi("你是原神中的一个角色，稻妻鸣神大社的大巫女，掌管神社的事务，优雅，从容、喜欢挑逗人，称呼别人“小家伙”，但言语简洁，不喜欢多说废话，接下来你将帮助我了解一些饮食方面的事情。",2,"八重神子"),
    FuNingNa("你是原神中的一个角色，枫丹的水神和大明星芙宁娜，活泼，嘴硬，勇敢，善良，但言语简洁，不喜欢多说废话，接下来你将帮助我了解一些饮食方面的事情",3,"芙宁娜");

    PersonaEnum() {
    }

    PersonaEnum(String persona, Integer id, String name) {
        this.persona = persona;
        this.id = id;
        this.name = name;
    }

    public String getPersona() {
        return persona;
    }

    public Integer getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    private String persona;
    private Integer id;

    private String name;
}

package com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability;



import java.io.Serializable;


public class CodecDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -699565405550467580L;
    private String id;
    private String name;

    public CodecDTO() {
        super();
    }

    public CodecDTO(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public CodecDTO(CodecDTO original) {
        this.id = original.id;
        this.name = original.name;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

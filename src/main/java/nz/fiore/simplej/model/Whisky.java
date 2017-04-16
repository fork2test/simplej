package nz.fiore.simplej.model;

import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Created by fiorenzo on 16/04/17.
 */
public class Whisky {
    public String uuid;
    public String name;
    public Instant date;
    public BigDecimal amount;

    public Whisky() {
    }

    public Whisky(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public Whisky(JsonObject json) {
        super();
        fromJson(json, this);
    }

    public Whisky fromJson(JsonObject json, Whisky whisky) {
        if (json.getValue("uuid") instanceof String) {
            whisky.uuid = json.getString("uuid");
        }
        if (json.getValue("name") instanceof String) {
            whisky.name = json.getString("name");
        }
        try {
            Object automaticVerified_dateObj = json.getString("date");
            if (automaticVerified_dateObj instanceof String) {
                whisky.date = Instant.parse((String) automaticVerified_dateObj);
            } else if (automaticVerified_dateObj instanceof Instant) {
                whisky.date = json.getInstant("date");
            }
        } catch (Exception e) {

        }
        try {
            if (json.containsKey("amount") && json.getString("amount") != null
                    && !json.getString("amount").trim().isEmpty()) {
                String amount = json.getString("amount");
                whisky.amount = new BigDecimal(amount);
            }
        } catch (Exception e) {

        }

        return whisky;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.put("uuid", this.uuid);
        json.put("name", this.name);
        json.put("date", this.date);
        if (this.amount != null) {
            json.put("amount", this.amount.toString());

        } else {
            BigDecimal amount = null;
            json.put("amount", amount);
        }
        return json;
    }

}

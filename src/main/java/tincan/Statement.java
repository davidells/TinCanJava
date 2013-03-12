package tincan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import tincan.json.JSONBase;
import tincan.json.Mapper;
import java.net.MalformedURLException;
import java.util.UUID;

/**
 * Statement Class
 */
@Data
@NoArgsConstructor
@Log
public class Statement extends JSONBase {
    private UUID id;
    private Agent actor;
    private Verb verb;
    private StatementTarget object;
    private Result result;
    private Context context;
    private DateTime timestamp;
    private DateTime stored;
    private Agent authority;
    private Boolean voided;
    private Boolean inProgress;

    @Setter(AccessLevel.PROTECTED)
    private String _json;

    public Statement (JsonNode jsonNode) throws MalformedURLException {
        this();

        log.info("constructor (from JsonNode)");
        JsonNode idNode = jsonNode.path("id");
        if (! idNode.isMissingNode()) {
            this.setId(UUID.fromString(idNode.textValue()));
        }

        JsonNode actorNode = jsonNode.path("actor");
        if (! actorNode.isMissingNode()) {
            // TODO: check for Group (objectType)
            this.setActor(new Agent(actorNode));
        }

        JsonNode verbNode = jsonNode.path("verb");
        if (! verbNode.isMissingNode()) {
            this.setVerb(new Verb(verbNode));
        }

        JsonNode objectNode = jsonNode.path("object");
        if (! objectNode.isMissingNode()) {
            JsonNode objectTypeNode = objectNode.path("objectType");
            if (objectTypeNode.textValue().equals("Activity")) {
                this.setObject(new Activity(objectNode));
            }
        }

        JsonNode timestampNode = jsonNode.path("timestamp");
        if (! timestampNode.isMissingNode()) {
            this.setTimestamp(new DateTime(timestampNode.textValue()));
        }

        JsonNode storedNode = jsonNode.path("stored");
        if (! storedNode.isMissingNode()) {
            this.setStored(new DateTime(storedNode.textValue()));
        }

        JsonNode authorityNode = jsonNode.path("authority");
        if (! authorityNode.isMissingNode()) {
            this.setAuthority(new Agent(authorityNode));
        }
    }

    public Statement (String json) throws Exception {
        this(Mapper.getInstance().readValue(json, JsonNode.class));

        log.info("constructor (from String)");
        this.set_json(json);
    }

    @Override
    public ObjectNode toJSONNode(TCAPIVersion version) {
        log.info("toJSONNode - version: " + version.toString());
        ObjectNode node = Mapper.getInstance().createObjectNode();
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

        if (this.id != null) {
            node.put("id", this.getId().toString());
        }
        if (this.timestamp != null) {
            node.put("timestamp", fmt.print(this.getTimestamp()));
        }
        if (this.stored != null) {
            node.put("stored", fmt.print(this.getStored()));
        }
        node.put("actor", this.getActor().toJSONNode(version));
        node.put("verb", this.getVerb().toJSONNode(version));
        node.put("object", this.getObject().toJSONNode(version));

        if (this.authority != null) {
            node.put("authority", this.getAuthority().toJSONNode(version));
        }

        return node;
    }

    /**
     * Method to set a random ID and the current date/time in the 'timestamp'
     */
    public void stamp() {
        if (this.id == null) {
            this.setId(UUID.randomUUID());
        }
        if (this.timestamp == null) {
            this.setTimestamp(new DateTime());
        }
    }
}

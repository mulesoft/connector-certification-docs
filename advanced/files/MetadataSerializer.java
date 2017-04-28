import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.mule.common.metadata.*;
import org.mule.common.metadata.datatype.DataType;
import org.mule.common.metadata.field.property.MetaDataFieldProperty;
import org.mule.common.metadata.field.property.ValidStringValuesFieldProperty;

import java.util.List;
import java.util.Set;

public class MetadataSerializer {

    private final MetaData metadata;

    public MetadataSerializer(@NotNull final MetaData metadata) {
        this.metadata = metadata;
    }

    public String toJson() {

        final JsonTransformer transformer = new JsonTransformer();
        final JSONObject json = transformer.generate(metadata);
        return json.toString(2);

    }

    private static class JsonTransformer {

        public JSONObject generate(@NotNull final MetaData metaData) {
            final JSONObject result = new JSONObject();

            // Serialize properties ...
            final MetaDataPropertyScope[] values = MetaDataPropertyScope.values();
            for (final MetaDataPropertyScope type : values) {
                final MetaDataProperties properties = metaData.getProperties(type);
                if (!properties.getFields().isEmpty()) {
                    result.put(type.name().toLowerCase(), generate(properties));
                }
            }

            // Return Structure type ..
            final MetaDataModel payload = metaData.getPayload();
            result.put("payload-type", payload.getDataType().name().toLowerCase());

            // Generate payload structure ..
            result.put("payload", generate((DefaultDefinedMapMetaDataModel) payload));

            return result;

        }

        @NotNull public JSONObject generate(@NotNull final MetaDataProperties properties) {
            final JSONObject result = new JSONObject();
            final Set<MetaDataField> fields = properties.getFields();
            for (MetaDataField field : fields) {
                result.put(field.getName(), field.getProperties());
            }
            return result;
        }

        @NotNull public JSONObject generate(@NotNull final DefaultDefinedMapMetaDataModel model) {
            final JSONObject result = new JSONObject();

            // Field children properties ...
            final List<MetaDataField> fields = model.getFields();
            for (MetaDataField field : fields) {

                final String key = field.getName() != null ? field.getName() : "UNDEFINED";
                final MetaDataModel dataModel = field.getMetaDataModel();
                if (dataModel instanceof DefaultDefinedMapMetaDataModel) {
                    result.put(key, generate((DefaultDefinedMapMetaDataModel) dataModel));
                } else {

                    // What is the type ?
                    final DataType dataType = dataModel.getDataType();
                    final String typeStr;
                    switch (dataType) {
                        case POJO:
                        case ENUM: {
                            typeStr = dataType.getDefaultImplementationClass();
                            break;
                        }
                        default: {
                            typeStr = dataType.name().toLowerCase();
                        }
                    }

                    // Append Properties ?
                    final JSONObject props = generate(field.getProperties());
                    Object fieldDesc;
                    if (props.keySet().isEmpty()) {
                        fieldDesc = typeStr;
                    } else {
                        final JSONObject value = new JSONObject();
                        value.put("type", typeStr);
                        value.put("props", props);
                        fieldDesc = value;
                    }
                    result.put(key, fieldDesc);

                }
            }

            return result;
        }

        @NotNull private JSONObject generate(@NotNull List<MetaDataFieldProperty> properties) {
            final JSONObject result = new JSONObject();
            for (MetaDataFieldProperty property : properties) {

                if (property instanceof ValidStringValuesFieldProperty) {
                    ValidStringValuesFieldProperty validProps = (ValidStringValuesFieldProperty) property;
                    result.put("valid-values", validProps.getValidStrings());
                }
            }
            return result;

        }
    }
}
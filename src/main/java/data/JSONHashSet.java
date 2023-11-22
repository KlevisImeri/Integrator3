package data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashSet;

public class JSONHashSet<T extends JsonConvertible> extends HashSet<T> implements JsonConvertible {

    private final Class<T> elementType;

    public JSONHashSet(Class<T> elementType) {
        this.elementType = elementType;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONArray jsonArray = new JSONArray();
        for (T item : this) {
            jsonArray.add(item.getJSONObject());
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("expressions", jsonArray);
        return jsonObject;
    }

    @Override
    public void setJSONObject(JSONObject jsonObject) {
        clear();
        JSONArray expressionsArray = (JSONArray) jsonObject.get("expressions");

        for (Object entryObj : expressionsArray) {
            try {
                T item = elementType.newInstance();
                item.setJSONObject((JSONObject) entryObj);
                add(item);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

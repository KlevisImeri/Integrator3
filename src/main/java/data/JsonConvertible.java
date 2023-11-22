package data;

import org.json.simple.JSONObject;

interface JsonConvertible {
    JSONObject getJSONObject();
    void setJSONObject(JSONObject jsonObject);
}
package com.yf.remotecontrolserver.util;


import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import com.yf.remotecontrolserver.domain.Action;
import com.yf.remotecontrolserver.domain.Boot;
import com.yf.remotecontrolserver.domain.Gateway;
import com.yf.remotecontrolserver.domain.Palpitation;
import com.yf.remotecontrolserver.domain.Position;
import com.yf.remotecontrolserver.domain.Writer;

public class JsonAssistant {
    private JSONObject jsonObject;

    /**
     * 解析
     *
     * @return
     */
    public Boot getBoot(String data) {
        Boot b = null;
        try {
            jsonObject = new JSONObject(data);
            Gson gson = new Gson();
            b = gson.fromJson(data, Boot.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public Gateway paseGateway(String data) {
        Gateway g = new Gateway();
        try {
            jsonObject = new JSONObject(data);
            g.setKey(jsonObject.getString("key"));
            g.setGwID(jsonObject.getString("gwID"));
            g.setGwIp(jsonObject.getString("gwIP"));
            g.setGwPort(Integer.parseInt(jsonObject.getString("gwPort")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return g;
    }

    public Position pasePosition(String data) {
        Position p = null;
        try {
            jsonObject = new JSONObject(data);
            p = new Position(jsonObject.getString("cmd"), jsonObject.getInt("x"), jsonObject.getInt("y"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    /**
     * @param //data
     * @return
     */
    public String paseBoot(Boot boot) {
        String json = null;
        try {
            Gson gson = new Gson();
            json = gson.toJson(boot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public JSONObject paseJsonObject(String data) {
        try {
            return new JSONObject(data);
        } catch (JSONException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 生成
     */
    public String createMessage(Gateway gateway) {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("key", gateway.getKey());
            jsonObject.put("gwID", gateway.getGwID());
            jsonObject.put("gwIP", gateway.getGwIp());
            jsonObject.put("gwPort", gateway.getGwPort() + "");
            return jsonObject.toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String createBoot(Boot boot) {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("cmd", boot.getCmd());
            jsonObject.put("devid", boot.getDevid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 解析action
     */
    public Action paseAction(String data) {
        Action action = new Action();
        try {
            jsonObject = new JSONObject(data);
            action.setCmd(jsonObject.getString("cmd"));
            action.setData(jsonObject.getString("data"));
            return action;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return action;
    }

    public Palpitation pasePalpitation(String data) {
        Palpitation actionPalpitation = new Palpitation();
        try {
            jsonObject = new JSONObject(data);
            actionPalpitation.setCmd(jsonObject.getString("cmd"));
            actionPalpitation.setIp(jsonObject.getString("ip"));
            return actionPalpitation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actionPalpitation;
    }

    /**
     * 创建心跳
     *
     * @return
     */
    public String createPalpitation(Palpitation palpitation) {
        try {
            jsonObject = new JSONObject();
            jsonObject.put("cmd", palpitation.getCmd());
            jsonObject.put("ip", palpitation.getIp());
            return jsonObject.toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public Writer paseWriter(String data) {
        Writer writer = new Writer();
        try {
            jsonObject = new JSONObject(data);
            writer.setCmd(jsonObject.getString("cmd"));
            writer.setData(jsonObject.getString("data"));
            return writer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;
    }


}

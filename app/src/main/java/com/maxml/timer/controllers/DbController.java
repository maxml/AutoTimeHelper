package com.maxml.timer.controllers;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.maxml.timer.database.ActionDAO;
import com.maxml.timer.database.CoordinateDAO;
import com.maxml.timer.database.DBFactory;
import com.maxml.timer.database.PathDAO;
import com.maxml.timer.database.TableDAO;
import com.maxml.timer.database.UserDAO;
import com.maxml.timer.database.WifiStateDAO;
import com.maxml.timer.entity.Action;
import com.maxml.timer.entity.Coordinates;
import com.maxml.timer.entity.Events;
import com.maxml.timer.entity.Path;
import com.maxml.timer.entity.Table;
import com.maxml.timer.entity.WifiState;
import com.maxml.timer.util.Constants;
import com.maxml.timer.util.NetworkUtil;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class DbController {

    private static EventBus serviceEventBus;
    private EventBus entityEventBus;
    private Context context;

    // db
    private ActionDAO actionDAO;
    private TableDAO tableDAO;
    private PathDAO pathDAO;
    private CoordinateDAO coordinateDAO;
    private UserDAO userDAO;
    private WifiStateDAO wifiStateDAO;


    public DbController(Context context, EventBus entityEventBus) {
        this.context = context;
        this.entityEventBus = entityEventBus;
        initCRUD();
    }

    public static DbController build(Context context, EventBus eventBus) {
        serviceEventBus = eventBus;
        return new DbController(context, eventBus);
    }

    public void createAction(Action action) {
        actionDAO.create(action);
    }

    public void createWalkAction(Action walk) {
        actionDAO.createWalkAction(walk);
    }


    public void sendDbResultOk() {
        entityEventBus.post(new Events.DbResult(Constants.EVENT_DB_RESULT_OK));
    }

    public void sendDbResultError() {
        entityEventBus.post(new Events.DbResult(Constants.EVENT_DB_RESULT_ERROR));
    }

    public void getPathFromDb(List<String> listIdPath) {
        List<Path> result = pathDAO.getPathById(listIdPath);

        entityEventBus.post(result);
    }

    public void getPathFromDb(String idPath) {
        Path path = pathDAO.getPathById(idPath);

        entityEventBus.post(path);
    }

    public void getTableFromDb(Date date) {
        tableDAO.getTableByData(date);
    }

    public void getTableFromDb(Date startDate, Date endDate) {
        tableDAO.getTableByData(startDate, endDate);
    }

    public void getActionFromDb(String id) {
        actionDAO.getActionFromDb(id);
    }

    public void removeActionInDb(String id) {
        actionDAO.removeAction(id);
    }

    public void sendTableFromDb(Table table) {
        entityEventBus.post(table);
    }

    public void sendTableFromDb(List<Table> tableList) {
        entityEventBus.post(tableList);
    }

    public void sendActionFromDb(Action action) {
        entityEventBus.post(action);
    }

    public void updateActionInDb(Action action) {
        actionDAO.updateActionInDb(action);
    }

    public void updateUserEmail(String email) {
        userDAO.updateEmail(email);
    }

    public void updateUserName(String name) {
        userDAO.updateName(name);
    }

    public void updateUserPhoto(String uri) {
        userDAO.updatePhoto(Uri.parse(uri));
    }

    public void createUser(String email, String password) {
        userDAO.create(email, password);
    }

    public void savePath(String pathId, List<Coordinates> coordinates) {
        Path path = new Path(pathId);
        for (Coordinates coordinate : coordinates) {
            coordinate.setPath(path);
        }
        coordinateDAO.save(coordinates);
        pathDAO.save(path);
    }

    public void getCurrentUser() {
        entityEventBus.post(UserDAO.getCurrentUser());
    }

    public void login(String email, String password) {
        userDAO.login(email, password);
    }

    public void loginAnonymously() {
        userDAO.loginAnonymously();
    }

    public void logout() {
        userDAO.logout();
    }

    public void forgotPassword(String email) {
        userDAO.sentPassword(email);
    }

    public void endSavingWalkAction(String walkActionId) {
        serviceEventBus.post(new Events.DbResult(Constants.EVENT_WALK_ACTION_SAVED, walkActionId));
    }

    public void wifiActivated(WifiState wifiState) {
        wifiStateDAO.insertOrUpdateData(wifiState);
    }

    public int getWifiTypeFromDB(WifiState wifi) {
        WifiState wifiState = wifiStateDAO.getWifiStatesById(wifi.getId());

        if (wifiState != null) {
            return wifiState.getType();
        }
        return Constants.WIFI_TYPE_NONE;
    }

    public void updateWifi(WifiState wifiState){
        wifiStateDAO.updateData(wifiState);
    }

    public void getAllWifi() {
        entityEventBus.post(wifiStateDAO.getAllRoles());
    }

    public void registerEventBus(EventBus entityEventBus) {
        serviceEventBus.post(new Events.EventBusControl(Constants.EVENT_REGISTER_EVENT_BUS, entityEventBus));
    }

    public void unregisterEventBus(EventBus entityEventBus) {
        serviceEventBus.post(new Events.EventBusControl(Constants.EVENT_UNREGISTER_EVENT_BUS, entityEventBus));
    }

    private void initCRUD() {
        tableDAO = new TableDAO(this);
        actionDAO = new ActionDAO(this);
        userDAO = new UserDAO(this);
        wifiStateDAO = DBFactory.getHelper().getWifiStateDAO();
        pathDAO = DBFactory.getHelper().getPathDAO();
        coordinateDAO = DBFactory.getHelper().getCoordinateDAO();
    }
}

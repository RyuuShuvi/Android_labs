package ua.cn.stu.getvariant.converter.lab2;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import ua.cn.stu.getvariant.converter.R;

import android.content.res.Resources;
import java.util.Iterator;


public class ConversionService extends Service {
    private final IBinder binder = new LocalBinder();
    private Map<String, Double> conversionRates;
    private Map<String, Map<String, Double>> temperatureConversions;
    private Map<String, String> unitKeys;

    @Override
    public void onCreate() {
        super.onCreate();
        loadConversionRates();
        loadUnitKeys();
    }

    private void loadConversionRates() {
        conversionRates = new HashMap<>();
        temperatureConversions = new HashMap<>();
        try {
            Resources res = getResources();
            InputStream inputStream = res.openRawResource(R.raw.conversion_rates);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);

            // Завантаження коефіцієнтів для ваги та довжини
            loadRatesFromJSON(jsonObject.getJSONObject("weight"), "weight");
            loadRatesFromJSON(jsonObject.getJSONObject("length"), "length");

            // Завантаження коефіцієнтів для температури
            JSONObject temperatureJson = jsonObject.getJSONObject("temperature");
            Iterator<String> keys = temperatureJson.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject tempObject = temperatureJson.getJSONObject(key);
                Map<String, Double> conversions = new HashMap<>();
                Iterator<String> tempKeys = tempObject.keys();
                while (tempKeys.hasNext()) {
                    String tempKey = tempKeys.next();
                    conversions.put(tempKey, tempObject.getDouble(tempKey));
                }
                temperatureConversions.put(key, conversions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRatesFromJSON(JSONObject jsonObject, String category) throws Exception {
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            double value = jsonObject.getDouble(key);
            conversionRates.put(key, value);
        }
    }

    private void loadUnitKeys() {
        unitKeys = new HashMap<>();
        unitKeys.put("Сантиметр", "centimeter");
        unitKeys.put("Метр", "meter");
        unitKeys.put("Кілометр", "kilometer");
        unitKeys.put("Дюйм", "inch");
        unitKeys.put("Миля", "mile");
        unitKeys.put("Ярд", "yard");
        unitKeys.put("Фут", "foot");
        unitKeys.put("Грам", "gram");
        unitKeys.put("Кілограм", "kilogram");
        unitKeys.put("Тона", "ton");
        unitKeys.put("Карат", "carat");
        unitKeys.put("Фунт", "pound");
        unitKeys.put("Пуд", "pood");
        unitKeys.put("Кельвін", "kelvin");
        unitKeys.put("Цельсій", "celsius");
        unitKeys.put("Фаренгейт", "fahrenheit");
    }

    public double convert(double inputValue, String fromUnit, String toUnit) {
        String fromKey = unitKeys.get(fromUnit);
        String toKey = unitKeys.get(toUnit);
        if (fromKey == null || toKey == null) {
            return inputValue; // Повертаємо вхідне значення, якщо ключі не знайдено
        }

        // Перевірка для температурних конверсій
        String temperatureKey = fromKey + "_to_" + toKey;
        if (temperatureConversions.containsKey(temperatureKey)) {
            Map<String, Double> tempConversion = temperatureConversions.get(temperatureKey);
            return performTemperatureConversion(tempConversion, inputValue);
        }

        // Перевірка для вагових та довжинних конверсій
        String key = fromKey + "_to_" + toKey;
        if (conversionRates.containsKey(key)) {
            return inputValue * conversionRates.get(key);
        } else {
            return inputValue; // Якщо коефіцієнт не знайдено, повертаємо вхідне значення
        }
    }

    private double performTemperatureConversion(Map<String, Double> conversionMap, double inputValue) {
        double result = inputValue;
        if (conversionMap.containsKey("intermediate_to_celsius")) {
            Map<String, Double> intermediateConversion = new HashMap<>();
            intermediateConversion.put("multiplier", conversionMap.get("intermediate_to_celsius"));
            intermediateConversion.put("subtraction", conversionMap.get("subtraction"));
            result = performTemperatureConversion(intermediateConversion, result);
            result += 273.15; // Перетворення до Кельвіна
        }
        if (conversionMap.containsKey("multiplier")) {
            result *= conversionMap.get("multiplier");
        }
        if (conversionMap.containsKey("addition")) {
            result += conversionMap.get("addition");
        }
        if (conversionMap.containsKey("subtraction")) {
            result -= conversionMap.get("subtraction");
        }
        return result;
    }

    public class LocalBinder extends Binder {
        public ConversionService getService() {
            return ConversionService.this;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}

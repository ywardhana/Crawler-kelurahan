/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corpus;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Dell XPS 13
 */
public class Kelurahan {

    private Map<String, Map<String, Map<String, Set<String>>>> daftarKelurahan;

    public Kelurahan() {
        daftarKelurahan = new TreeMap<String, Map<String, Map<String, Set<String>>>>();
    }
    
    public Map<String, Map<String, Map<String, Set<String>>>> getData() {
        return daftarKelurahan;
    }

    public void put(String province, String city, String kecamatan, String kelurahan) {
        Map<String, Map<String, Set<String>>> daftarKota = daftarKelurahan.get(province);
        if (daftarKota == null) {
            daftarKota = new TreeMap<String, Map<String, Set<String>>>();
            daftarKelurahan.put(province, daftarKota);
        }
        Map<String, Set<String>> daftarKecamatan = daftarKota.get(city);
        kelolaKecamatan(daftarKota, daftarKecamatan, city, kecamatan, kelurahan);

    }
    
    public Set<String> get(String province, String city, String kecamatan) {
        return daftarKelurahan.get(province).get(city).get(kecamatan);
    }

    private void kelolaKecamatan(Map<String, Map<String, Set<String>>> daftarKota, Map<String, Set<String>> daftarKecamatan, String kota, String kecamatan, String kelurahan) {
        if (daftarKecamatan == null) {
            daftarKecamatan = new TreeMap<String, Set<String>>();
            daftarKota.put(kota, daftarKecamatan);
        }
        Set<String> daftarKelurahan = daftarKecamatan.get(kecamatan);
        kelolaKelurahan(daftarKecamatan, daftarKelurahan, kecamatan, kelurahan);
    }

    private void kelolaKelurahan(Map<String, Set<String>> daftarKecamatan, Set<String> daftarKelurahan, String kecamatan, String kelurahan) {
        if (daftarKelurahan == null) {
            daftarKelurahan = new HashSet<String>();
            daftarKecamatan.put(kecamatan, daftarKelurahan);
        }
        daftarKelurahan.add(kelurahan);
    }
}

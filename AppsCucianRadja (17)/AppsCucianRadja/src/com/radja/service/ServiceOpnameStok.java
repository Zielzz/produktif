package com.radja.service;

import com.radja.model.OpnameStok;
import com.radja.model.DetailOpnameStok;
import java.util.List;

public interface ServiceOpnameStok {
    void insertOpnameStok(OpnameStok opname, List<DetailOpnameStok> details);
    List<OpnameStok> getAllOpnameStok();
    List<DetailOpnameStok> getDetailOpnameStok(int idOpname);
}
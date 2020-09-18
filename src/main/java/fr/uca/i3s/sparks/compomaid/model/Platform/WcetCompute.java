package fr.uca.i3s.sparks.compomaid.model.Platform;

import fr.uca.i3s.sparks.compomaid.model.App.AcqMode.Data.Data;

import java.util.ArrayList;

public interface WcetCompute {

    double compute(ArrayList<Data> dataList);

}

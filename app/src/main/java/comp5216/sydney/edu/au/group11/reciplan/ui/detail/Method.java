package comp5216.sydney.edu.au.group11.reciplan.ui.detail;

import java.util.ArrayList;
import java.util.List;

import comp5216.sydney.edu.au.group11.reciplan.materials.AnalyzedInstruction;
import comp5216.sydney.edu.au.group11.reciplan.materials.Step;

public class Method {
    private final List<AnalyzedInstruction> data;

    public Method(List<AnalyzedInstruction> data) {
        this.data = data;
    }

    public List<AnalyzedInstruction> getData() {
        return data;
    }

    @Override
    public String toString() {
        String string = "";
        AnalyzedInstruction instruction = data.get(0);
        ArrayList<Step> steps = instruction.getSteps();
        for(int i = 0; i < steps.size(); i++){
            Step step = steps.get(i);
            string += "<b>Step " + step.getNumber() + "</b><br>";
            string += step.getStep();
            string += "<br>";
            if(step.getLength() != null) {
                string += "<b>" + step.getLength().toString() + "</b>";
            }
            string += "<br>" + step.showEquipment() + "<br>";
            string += step.showIngredients();
            string += "<br><br>";
        }
        return string;
    }
}

package com.example.learnclient;

import com.example.learnclient.modules.EspModule;
import com.example.learnclient.modules.FlyModule;
import com.example.learnclient.modules.FullbrightModule;
import com.example.learnclient.modules.KillAuraModule;
import com.example.learnclient.modules.Module;
import com.example.learnclient.modules.SpeedModule;
import com.example.learnclient.modules.SprintModule;
import com.example.learnclient.modules.StepModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Хранит список модулей и обновляет их каждый тик.
 */
public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public void init() {
        modules.add(new KillAuraModule());
        modules.add(new SprintModule());
        modules.add(new FlyModule());
        modules.add(new SpeedModule());
        modules.add(new StepModule());
        modules.add(new FullbrightModule());
        modules.add(new EspModule());
        modules.add(new com.example.learnclient.modules.AutoRespawnModule());
        modules.add(new com.example.learnclient.modules.DeathCoordsModule());
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModules(Module.Category category) {
        List<Module> result = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory() == category) result.add(m);
        }
        return result;
    }

    public void tick() {
        for (Module m : modules) {
            if (m.isEnabled()) m.onTick();
        }
    }
}

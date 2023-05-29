package net.starly.reverietoken;

import lombok.Getter;
import net.starly.core.bstats.Metrics;
import net.starly.reverietoken.command.ReverieTokenExecutor;
import net.starly.reverietoken.context.MessageContent;
import net.starly.reverietoken.listener.PlayerInteractListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ReverieToken extends JavaPlugin {

    @Getter private static ReverieToken instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        if (!isPluginEnable("ST-Core")) {
            getServer().getLogger().warning("[" + getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            getServer().getLogger().warning("[" + getName() + "] 다운로드 링크 : http://starly.kr/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new Metrics(this, 18602);

        // CONFIG
        saveDefaultConfig();
        MessageContent.getInstance().initialize(getConfig());

        // LISTENER & COMMAND
        getCommand("귀환토큰").setExecutor(new ReverieTokenExecutor());
        getCommand("귀환토큰").setTabCompleter(new ReverieTokenExecutor());

        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
    }

    private boolean isPluginEnable(String pluginName) {
        Plugin plugin = getServer().getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }
}

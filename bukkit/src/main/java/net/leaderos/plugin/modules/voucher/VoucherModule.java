package net.leaderos.plugin.modules.voucher;

import lombok.Getter;
import net.leaderos.plugin.Bukkit;
import net.leaderos.plugin.modules.voucher.commands.VoucherCommand;
import net.leaderos.plugin.modules.voucher.listeners.VoucherListener;
import net.leaderos.plugin.modules.voucher.data.Data;
import net.leaderos.shared.modules.LeaderOSModule;
import org.bukkit.event.HandlerList;

/**
 * Voucher module of leaderos plugin
 * @author poyrazinan
 * @since 1.0
 */
public class VoucherModule extends LeaderOSModule {

    /**
     * Voucher data folder
     */
    @Getter
    private static Data voucherData;

    /**
     * Listener of voucher
     */
    private static VoucherListener voucherListener;

    /**
     * onEnable method of module
     */
    public void onEnable() {
        (voucherData = new Data("voucher_data.yml")).create();
        voucherListener = new VoucherListener();
        org.bukkit.Bukkit.getPluginManager().registerEvents(voucherListener, Bukkit.getInstance());
        Bukkit.getCommandManager().registerCommand(new VoucherCommand());
    }

    /**
     * onDisable method of module
     */
    public void onDisable() {
        HandlerList.unregisterAll(voucherListener);
        Bukkit.getCommandManager().unregisterCommand(new VoucherCommand());
    }

    /**
     * Constructor of Voucher class
     */
    public VoucherModule() { addDependency("Credit"); }
}

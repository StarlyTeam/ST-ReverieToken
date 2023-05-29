package net.starly.reverietoken.listener;

import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;
import net.starly.reverietoken.ReverieToken;
import net.starly.reverietoken.context.MessageContent;
import net.starly.reverietoken.context.MessageType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @SuppressWarnings("ConstantConditions")
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!(event.getHand() == EquipmentSlot.HAND)) return;
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) return;

        ItemStackWrapper itemStackWrapper = NmsItemStackUtil.getInstance().asNMSCopy(itemStack);
        NBTTagCompoundWrapper nbtTagCompoundWrapper = itemStackWrapper.getTag();

        if (nbtTagCompoundWrapper == null) return;

        String locationString = nbtTagCompoundWrapper.getString("st-reverietoken");
        if (locationString == null || locationString.isEmpty()) return;

        event.setCancelled(true);

        String[] parts = locationString.split(",");
        if (parts.length != 6) {
            MessageContent.getInstance().getMessageAfterPrefix(MessageType.ERROR, "invalidLocationString").ifPresent(player::sendMessage);
            return;
        }

        try {
            World world = ReverieToken.getInstance().getServer().getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float pitch = Float.parseFloat(parts[4]);
            float yaw = Float.parseFloat(parts[5]);

            Location retrievedLocation = new Location(world, x, y, z, yaw, pitch);

            itemStack.setAmount(itemStack.getAmount() - 1);

            ReverieToken.getInstance().getServer().getScheduler().runTask(ReverieToken.getInstance(), () ->
                    player.teleport(retrievedLocation));

            MessageContent.getInstance().getMessageAfterPrefix(MessageType.NORMAL, "useReverieToken").ifPresent(player::sendMessage);
        } catch (NumberFormatException e) {
            MessageContent.getInstance().getMessageAfterPrefix(MessageType.ERROR, "incorrectLocationFormat").ifPresent(player::sendMessage);
        }
    }
}
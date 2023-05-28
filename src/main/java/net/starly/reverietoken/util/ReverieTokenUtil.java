package net.starly.reverietoken.util;

import net.starly.core.jb.version.nms.tank.NmsItemStackUtil;
import net.starly.core.jb.version.nms.wrapper.ItemStackWrapper;
import net.starly.core.jb.version.nms.wrapper.NBTTagCompoundWrapper;
import net.starly.reverietoken.context.MessageContent;
import net.starly.reverietoken.context.MessageType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ReverieTokenUtil {

    public static void setItemInHand(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemStackWrapper itemStackWrapper = NmsItemStackUtil.getInstance().asNMSCopy(itemStack);
        NBTTagCompoundWrapper nbtTagCompoundWrapper = itemStackWrapper.getTag();

        if (nbtTagCompoundWrapper == null) nbtTagCompoundWrapper = NmsItemStackUtil.getInstance().getNbtCompoundUtil().newInstance();

        Location location = player.getLocation();
        String locationString = location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getPitch() + "," + location.getYaw();

        nbtTagCompoundWrapper.setString("st-reverietoken", locationString);
        itemStackWrapper.setTag(nbtTagCompoundWrapper);

        ItemMeta itemMeta = NmsItemStackUtil.getInstance().asBukkitCopy(itemStackWrapper).getItemMeta();

        MessageContent content = MessageContent.getInstance();

        if (itemMeta == null) {
            content.getMessageAfterPrefix(MessageType.ERROR, "noItemInHand").ifPresent(player::sendMessage);
            return;
        }

        itemStack.setItemMeta(itemMeta);

        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), itemStack);
        content.getMessageAfterPrefix(MessageType.NORMAL, "setReverieToken").ifPresent(player::sendMessage);
    }
}

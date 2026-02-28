package com.winnersystems.smartparking.parking.application.port.input.space;

/**
 * Puerto de entrada para eliminar un espacio de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface DeleteSpaceUseCase {
   void deleteSpace(Long spaceId);
}
package ar.com.umibe.gui;

import java.awt.dnd.*;
import java.awt.datatransfer.*;

import javax.swing.JList;
import javax.swing.DefaultListModel;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public abstract class DragAndDropList extends JList implements
		DragAndDropComponentInterface, DropTargetListener, DragSourceListener,
		DragGestureListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1866680077617519768L;

	/**
	 * enables this component to be a dropTarget
	 */

	DropTarget dropTarget = null;

	/**
	 * enables this component to be a Drag Source
	 */
	DragSource dragSource = null;

	/**
	 * constructor - initializes the DropTarget and DragSource.
	 */

	public DragAndDropList() {

		this.dropTarget = new DropTarget(this, this);
		this.dragSource = new DragSource();
		this.dragSource.createDefaultDragGestureRecognizer(this,
				DnDConstants.ACTION_MOVE, this);
	}

	/**
	 * is invoked when you are dragging over the DropSite
	 * 
	 */

	public void dragEnter(DropTargetDragEvent event) {

		// debug messages for diagnostics
		System.out.println("dragEnter");
		event.acceptDrag(DnDConstants.ACTION_MOVE);
	}

	/**
	 * is invoked when you are exit the DropSite without dropping
	 * 
	 */

	public void dragExit(DropTargetEvent event) {
		System.out.println("dragExit");

	}

	/**
	 * is invoked when a drag operation is going on
	 * 
	 */

	public void dragOver(DropTargetDragEvent event) {
		System.out.println("dragOver");
	}

	/**
	 * a drop has occurred
	 * 
	 */

	public abstract void drop(DropTargetDropEvent event);

	/**
	 * is invoked if the use modifies the current drop gesture
	 * 
	 */

	public void dropActionChanged(DropTargetDragEvent event) {
	}

	/**
	 * a drag gesture has been initiated
	 * 
	 */

	public void dragGestureRecognized(DragGestureEvent event) {

		Object selected = getSelectedValue();
		if (selected != null) {
			StringSelection text = new StringSelection(selected.toString());

			// as the name suggests, starts the dragging
			this.dragSource.startDrag(event, DragSource.DefaultMoveDrop, text,
					this);
		} else {
			System.out.println("nothing was selected");
		}
	}

	/**
	 * this message goes to DragSourceListener, informing it that the dragging
	 * has ended
	 * 
	 */

	public void dragDropEnd(DragSourceDropEvent event) {
		if (event.getDropSuccess()) {
			removeElement();
		}
	}

	/**
	 * this message goes to DragSourceListener, informing it that the dragging
	 * has entered the DropSite
	 * 
	 */

	public void dragEnter(DragSourceDragEvent event) {
		System.out.println(" dragEnter");
	}

	/**
	 * this message goes to DragSourceListener, informing it that the dragging
	 * has exited the DropSite
	 * 
	 */

	public void dragExit(DragSourceEvent event) {
		System.out.println("dragExit");

	}

	/**
	 * this message goes to DragSourceListener, informing it that the dragging
	 * is currently ocurring over the DropSite
	 * 
	 */

	public void dragOver(DragSourceDragEvent event) {
		System.out.println("dragExit");

	}

	/**
	 * is invoked when the user changes the dropAction
	 * 
	 */

	public void dropActionChanged(DragSourceDragEvent event) {
		System.out.println("dropActionChanged");
	}

	/**
	 * adds elements to itself
	 * 
	 */

	public void addElement(Object s) {
		((DefaultListModel) getModel()).addElement(s.toString());
	}

	/**
	 * removes an element from itself
	 */

	public void removeElement() {
		((DefaultListModel) getModel()).removeElement(getSelectedValue());
	}

}

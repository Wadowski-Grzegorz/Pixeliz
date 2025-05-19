const Modal = ({ isOpen, onClose, children }) => {
    if(!isOpen) return null;

    return (
        <>
            <div onClick={onClose} className="fixed w-screen h-screen backdrop-blur-sm z-[99] top-0 left-0"/>
            <div className="fixed z-[101] top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 
                bg-[#575757] border border=[#767944] rounded-lg p-4 text-[#b8adb2]">
                {children}        
            </div>
        </>
    )
}

export default Modal;
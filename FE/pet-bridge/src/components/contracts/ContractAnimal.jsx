const ContractAnimal = ({imageSrc, name, kind, age}) => {
  return (
    <div className="col-span-2 rounded-l-xl border p-2.5">
      <p className="m-1 text-xl font-bold">입양 동물</p>
      <div className="flex space-x-5">
        <img src={imageSrc} alt={name} className="size-20 rounded-xl" />
        <div className="flex flex-col justify-between font-bold">
          <p>{name}</p>
          <p>{kind}</p>
          <p>{age}</p>
        </div>
      </div>
    </div>
  )
}

export default ContractAnimal
